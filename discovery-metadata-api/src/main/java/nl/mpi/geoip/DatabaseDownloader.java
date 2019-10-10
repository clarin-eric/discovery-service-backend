package nl.mpi.geoip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.zip.GZIPInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz
 * http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.md5
 *
 * @author wilelb
 */
public class DatabaseDownloader {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseDownloader.class);
    
    protected final File databaseGzipFile;
    protected final File databaseFile;
    protected final File databaseMd5File;
    
    public final static String DEFAULT_GEOLITE2_CITY_FILENAME = "GeoLiteCity.dat";
    
    protected final String geolite2_city_db = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz";
    protected final String geolite2_city_md5 = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.md5";
    
    public DatabaseDownloader(String geoIpDatabaseFile) {
        this.databaseFile = new File(geoIpDatabaseFile);
        this.databaseGzipFile = new File(geoIpDatabaseFile+".gz");
        this.databaseMd5File = new File(geoIpDatabaseFile+".md5");
    }
    
    public void checkForUpdate() {
        try {
            logger.info("Checking for geo-ip database update");
            if(!doesLocalFileExist()) {
                logger.info("No local database found, downloading files...");
                download();
            } else if(!doesChecksumMatch()) {
                logger.info("Newer remote database available, downloading files...");
                download();
            } else {
                logger.info("No update required");
            }
        } catch(IOException ex) {
            logger.error("Failed to check for updates", ex);
        }
    }
    
    private boolean doesLocalFileExist() {
        if(!databaseMd5File.exists()) {
            logger.warn("No database md5 file found, download forced");
        }
        return databaseFile.exists() && databaseMd5File.exists();
    }
    
    /**
     * Compare the remote and local checksum. Return true if the checksums match,
     * return false otherwise.
     * 
     * @return 
     */
    private boolean doesChecksumMatch() {
        try {
            String remoteChecksum = readRemoteChecksum();
            String localChecksum = readLocalChecksum();
            return remoteChecksum.compareTo(localChecksum) == 0;
        } catch(MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }        
    }
    
    private void download() throws IOException, FileNotFoundException {        
        //Check if all directories exist and create them of needed
        File parent = databaseFile.getParentFile();
        if(!parent.exists()) {
            if(parent.mkdirs()) {
                logger.info("Created directories for geo-ip database file [{}]", parent.getAbsolutePath());
            } else {
                throw new IOException("Failed to create directories for geo-ip database file ["+parent.getAbsolutePath()+"]");
            }
        }
        
        //Download database and md5 files
        downloadFile(new URL(geolite2_city_db), databaseGzipFile.toPath());
        downloadFile(new URL(geolite2_city_md5), databaseMd5File.toPath());
        //Decompress the database file
        decompress(databaseGzipFile, databaseFile);
    }
    
    private void downloadFile(URL url, Path path) throws IOException {
        boolean backupNeeded = false;
        Path tmp = null;
        if(path.toFile().exists()) {
            backupNeeded = true;
            tmp = path.getParent().resolve(path.getFileName()+".tmp");
        } else {
            backupNeeded = false;
            tmp = path;
        }
        
        Long maxBytesToRead = Long.MAX_VALUE;
        long totalBytesRead = 0L;
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setReadTimeout(10000);
        con.setConnectTimeout(10000);
        
        try (ReadableByteChannel rbc = Channels.newChannel(con.getInputStream())) {
            FileChannel fileChannel = FileChannel.open(tmp, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
            totalBytesRead = fileChannel.transferFrom(rbc, 0, maxBytesToRead);
            logger.trace("totalBytesRead = " + totalBytesRead);
            fileChannel.close();
        }
        
        if(backupNeeded) {
            //make backup of original file
            Files.move(path, path.getParent().resolve(path.getFileName()+".bak"), REPLACE_EXISTING);
        } 
        Files.move(tmp, path, REPLACE_EXISTING);    
    }
    
    private String readRemoteChecksum() throws MalformedURLException, IOException {
        URL url = new URL(geolite2_city_md5);
        return readChecksum(url.openStream());
    }
    
    private String readLocalChecksum() throws IOException {
        return readChecksum(new FileInputStream(databaseMd5File));
    }
    
    private String readChecksum(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        return br.readLine();
    }
    
    private void decompress(File gzipFile, File outputFile) throws IOException {
        byte[] buffer = new byte[1024];
        
        FileOutputStream out = null;
        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzipFile))) {
            out = new FileOutputStream(outputFile);
            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            if(out != null) {
                out.close();
            }
        }
    	
    }
}
