import json
import sys

if len(sys.argv) != 3:
    sys.stdout.write('usage: %s REQUESTS_FILE RESPONSES_FILE\n' % sys.argv[0])
    exit(1)

try:
    responses = {}
    with open(sys.argv[2]) as f:
        for line in f:
            response = json.loads(line)
            if set(response.keys()) != set(['id', 'body', 'runid', 'groupid']) or set(response['body'].keys()) != set(['suggestions']):
                sys.stderr.write('Each response should contain only a (request) ID, a list of suggestions, a runid, and a groupid not: %s\n' % ', '.join(response.keys()))
                exit(1)
            if response['id'] in responses:
                sys.stderr.write('Multiple responses for request ID %s\n' % response['id'])
                exit(1)
            responses[response['id']] = response

    with open(sys.argv[1]) as f:
        for line in f:
            request = json.loads(line)
            if request['id'] not in responses:
                sys.stderr.write('Response for request ID is missing %s\n' % request['id'])
                exit(1)
            if set(responses[request['id']]['body']['suggestions']) != set(request['candidates']):
                sys.stderr.write('Candidate and suggested documents do not match for request %s\n' % request['id'])
                exit(1)

    sys.stdout.write('Success.\n')
except Exception as e:
    sys.stderr.write(str(e) + '\n')
    exit(1)
