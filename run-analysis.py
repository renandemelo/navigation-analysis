#!/usr/bin/python

from config import *
import time
import sys
import os
import subprocess

def _exec(cmd):
	ps = subprocess.Popen(cmd, shell=True)
	ps.wait()

if __name__ == '__main__':
	navigations = ''
	for site in sites.keys():
		navigations += str(site + ":" + sites[site] + ",")
	navigations = navigations[:-1]

	nums = ''
	for i in num_clients:
		nums += str(i) + ','
	nums = nums[:-1]

	command = 'Command: java -jar "-Dnavigations=%s -Dnum_clients=%s" target/navigation-analysis.jar Analysis' % (navigations,nums)
	print command
	_exec(command)
