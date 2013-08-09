#! /usr/bin/python

import json
import sys


MYPROBLEMS = '../myproblems'


def main(argv):
    target_size = int(argv[1])

    with open(MYPROBLEMS, 'r') as fp:
        content = fp.read()
    problems = json.loads(content)
    for problem in problems:
        if problem['size'] == target_size:
            print json.dumps(problem)


if __name__ == '__main__':
    main(sys.argv)
