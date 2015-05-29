#!/usr/bin/env python

from __future__ import print_function
import sys, csv, re
import os.path

def usage():
    print("Usage: {0} resultsfile".format(sys.argv[0]))
    exit(1)

def error(msg):
    MAX_ERRORS = 25
    global num_errors
    string = "{0}: Error on line {1} --- {2}\n".format(results_file, line_num, msg)
    ERRLOG.write(string)

    num_errors += 1;
    if num_errors > MAX_ERRORS:
        string = "{0} of {1}: Quit. Too many errors!\n".format(sys.argv[0], results_file)
        ERRLOG.write(string)
        ERRLOG.close()
        exit(1)

def missingdata(s="data", loc=""):
   error("Missing {0}{1}. Result file fails vaildation test.".format(s, loc))
   ERRLOG.close()
   exit(1)

def validate_urls(numSuggestions):
    MAX_RET = 50
    for suggestion, item in numSuggestions.items():
        profile, context = suggestion
        num = len(item)
        if (num == 0):
            error("No URLs/docIds suggested for [profile {0}, context {1}]".format(profile, context))
        elif (len(set(item)) != num):
            error("Duplicate suggestion ranks for [profile {0}, context {1}]".format(profile, context))
        elif (num < MAX_RET):
            print("Warning: only {0} URLs/docIds suggested for [profile {1}, context {2}]".format(num, profile, context))
            if (max(item) > num):
                error("Non-consecutive ranks for [profile {0}, context {1}]".format(profile, context))

def main():
    global results_file, line_num, num_errors, ERRLOG
    try:
        path = sys.argv[1]
        results_file, ext = os.path.splitext(path)
    except:
        usage()

    if ext != '.csv':
        print("File extension must be csv. Result file fails validation test.")
        exit(1)

    line_num = 1
    num_errors = 0

    profiles = set(range(700, 999))
    contexts = set(range(101, 151))
    ranks = set(range(1, 51))

    urls = {}

    numSuggestions = {}
    docIdChecker = re.compile("clueweb12-[0-9]{4}[a-z]{2}-[0-9]{2}-[0-9]{5}")
    urlChecker = re.compile(
        r'^(?:http)s?://' # http:// or https://
        r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|' #domain...
        r'localhost|' #localhost...
        r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
        r'(?::\d+)?' # optional port
        r'(?:/?|[/?]\S+)$', re.IGNORECASE)
    for profile in profiles:
        for context in contexts:
            numSuggestions[(profile, context)] = []

    errlog_filename = results_file + ".errlog"
    try:
        ERRLOG = open(errlog_filename, 'w')
    except IOError:
        print("Cannot open error log for writing.")
        exit(1)
    try:
        results = open(path, 'r')
    except IOError:
        error("Cannot open results file.")
        ERRLOG.close()
        exit(1)

    data = csv.DictReader(results)
    try:
        row = next(data)
    except StopIteration:
        missingdata()
    try:
        groupid = row["groupid"]
        runid = row["runid"]
    except KeyError:
        missingdata("groupid/runid")
    try:
        allurl = row["url"] != ""
    except KeyError:
        missingdata("url")
    fieldnames = ['groupid', 'runid', 'profile', 'context', 'rank', 'title', 'description', 'url', 'docId']
    if data.fieldnames != fieldnames:
        error('Columns must be in order: {0}'.format(' '.join(fieldnames)))

    results.seek(0)
    next(data)

    try:
        for row in data:
            line_num = data.line_num - 2

            if (row["groupid"] != groupid or row["runid"] != runid):
                error("Groupids/runids not matching.")
                ERRLOG.close()
                exit(1)

            try:
                profile = row["profile"]
            except KeyError:
                missingdata("profile")
            try:
                context = row["context"]
            except KeyError:
                missingdata("context")
            try:
                rank = row["rank"]
            except KeyError:
                missingdata("rank")
            try:
                title = row["title"]
            except KeyError:
                missingdata("title", " for [profile {0}, context {1}, rank {2}]".format(profile, context, rank))
            try:
                desc = row["description"]
            except KeyError:
                missingdata("description", " for [profile {0}, context {1}, rank {2}]".format(profile, context, rank))
            try:
                url = row["url"]
            except KeyError:
                missingdata("url", " for [profile {0}, context {1}, rank {2}]".format(profile, context, rank))
            try:
                docId = row["docId"]
            except KeyError:
                missingdata("docId", " for [profile {0}, context {1}, rank {2}]".format(profile, context, rank))

            try:
                profile = int(profile)
            except ValueError:
                error("All profiles ids must be integers [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
            try:
                context = int(context)
            except ValueError:
                error("All context ids must be integers [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
            try:
                rank = int(rank)
            except ValueError:
                error("All rank positions must be integers [profile {0}, context {1}, rank {2}].".format(profile, context, rank))

            if (url != "" and docId != ""):
                error("All suggestions must be either urls or docIds [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
                continue

            if len(row) > 9:
                error("Too many columns for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))

            if(profile not in profiles):
                error("Invalid profile ID for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
                continue
            if(context not in contexts):
                error("Invalid context ID for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
                continue
            if(rank not in ranks):
                error("Invalid rank for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
                continue
            if(len(title) < 1 or len(title) > 64):
                error("Title must be non-empty and no more than 64 characters for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
            if(len(desc) < 1 or len(desc) > 512):
                error("Description must be non-empty and no more than 512 characters for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
            if('\n' in title or '\n' in desc):
                error("Newlines are not allowed for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))

            if allurl:
                if (url != ""):
                    if urlChecker.match(url) == None:
                        error("Invalid url for [profile {0}, context {1}, rank {2}]: {3}".format(profile, context, rank, url))
                        continue
                    suggestion = url
                else:
                    error("Missing url for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
                    continue
            else:
                if (docId != ""):
                    if docIdChecker.match(docId) == None:
                        error("Invalid docId for [profile {0}, context {1}, rank {2}]: {3}".format(profile, context, rank, docId))
                        continue
                    suggestion = docId
                else:
                    error("Missing docId for [profile {0}, context {1}, rank {2}].".format(profile, context, rank))
                    continue

            prof_cont = (profile, context)
            if suggestion in urls and prof_cont in urls[suggestion]:
                error("URL/docId {0} suggested more than once for [profile {1}, context {2}]".format(suggestion, profile, context))
                continue

            if suggestion not in urls:
                urls[suggestion] = set()

            urls[suggestion].add(prof_cont)
            numSuggestions[prof_cont].append(rank)

    except StopIteration:
        missingdata()

    if num_errors == 0:
        validate_urls(numSuggestions)

    print("Finished processing file.")
    try:
        ERRLOG.close()
    except IOError:
        print("Close failed for error log.")
        exit(1)

    if num_errors != 0:
        exit(1)

if __name__ == "__main__":
    main()
