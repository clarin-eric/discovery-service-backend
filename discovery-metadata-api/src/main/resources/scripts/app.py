#!/usr/bin/env python

import csv


def map(tld_file, country_code_file):
    tlds = loadTlds(tld_file)
    countries = loadCountries(country_code_file)

    matched = []
    unmatched = []
    for tld in tlds:
        code = getCountryCode(tld['country'], countries)
        if code:
            matched.append({'tld': tld['tld'], 'country': tld['country'], 'code': code})
        else:
            unmatched.append({'tld': tld['tld'], 'country': tld['country'], 'code': ''})

    write('/Users/wilelb/Downloads/matched.csv', matched)
    write('/Users/wilelb/Downloads/unmatched.csv', unmatched)


def write(output_file, data):
    rows = []
    for i in data:
        rows.append([i['tld'], i['country'], i['code']])

    f = open(output_file, 'w')
    try:
        w = csv.writer(f)
        w.writerows(rows)
    finally:
        f.close()


def getCountryCode(country_name, countries):
    code = None
    for country in countries:
        if country['name'] == country_name:
            code = country['code']
            break
    return code


def loadTlds(tld_file):
    tlds = []
    f = open(tld_file, 'rb')
    try:
        reader = csv.reader(f)
        for row in reader:
            tld = row[0].strip()
            country = row[1].strip()
            tlds.append({'tld': tld, 'country': country})
    finally:
        f.close()
    return tlds


def loadCountries(country_code_file):
    countries = []
    f = open(country_code_file, 'rb')
    try:
        reader = csv.reader(f)
        for row in reader:
            country = row[0].strip()
            code = row[1].strip()
            countries.append({'code': code, 'name': country})
    finally:
        f.close()
    return countries


if __name__ == '__main__':
    tld_file = '/Users/wilelb/Downloads/ccTLDs.csv'
    country_code_file = '/Users/wilelb/Downloads/countryCodes.csv'
    map(tld_file, country_code_file)