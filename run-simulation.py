#!/usr/bin/python

from config import *
import time
import sys
import os
import subprocess
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import CPULimitedHost
from mininet.node import OVSController
from mininet.node import OVSSwitch
from mininet.link import TCLink
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel

class LanTopo(Topo):
	def __init__(self, n=2, **opts):
		Topo.__init__(self, **opts)
		switch = self.addSwitch('switch1',listenPort=6634)
		server = self.addHost('server')
		self.addLink(server, switch,bw=10, delay='200ms', loss=2, use_htb=True)
		for c in range(n):
			# Each host gets 50%/n of system CPU
			host = self.addHost('client%s' % (c + 1),cpu=.5 / n)
			# 10 Mbps, 5ms delay, 10% loss
			self.addLink(host, switch,bw=100, delay='10ms', loss=2, use_htb=True)
	

def running():
	ps= subprocess.Popen("ps aux", shell=True, stdout=subprocess.PIPE)
   	output = ps.stdout.read()
	ps.stdout.close()
	ps.wait()
	return output

def _exec(cmd):
	ps = subprocess.Popen(cmd, shell=True)
	ps.wait()

def experiment(site,navigation,qtde_clients):
	if not os.path.exists('data'):
    		os.makedirs('data')
	directory = 'experiments/' + site + '-' + str(qtde_clients)
	print 'Executing test for: ' + directory
	_exec('rm -rf data/*')
	_exec('rm -rf /tmp/*')
	perfTest(navigation,qtde_clients)
	if not os.path.exists(directory):
    		os.makedirs(directory)
	_exec('rm -rf ' + directory + '/*')
	_exec('mv data/* ' + directory)
	

def perfTest(navigation,qtde):
	"Create network and run simple performance test"
	topo = LanTopo(n=qtde)
	net = Mininet(topo=topo,host=CPULimitedHost, link=TCLink,controller=OVSController,switch=OVSSwitch)
	net.start()
	print "Dumping host connections"
	
	server = net.get('server')
	print "server ip:" + server.IP()
	
	server.cmd( 'java -jar "-Dnavigation-file=%s" target/navigation-analysis.jar Server' % navigation,qtde,'>','/dev/null','&')
	time.sleep(5)
	for c in range(qtde):
		client = net.getNodeByName('client%s' % (c+1))
		client.cmd('java -jar "-Dnavigation-file=%s" target/navigation-analysis.jar Client' % navigation,server.IP(),'>', '/dev/null','&')#'>','client%s.txt' % (c+1),

	switch = net.get('switch1')
	while("java" in running()):
		print "Still running... "
		time.sleep(4)
	switch.cmdPrint('dpctl dump-ports tcp:' + switch.IP() + ':' + str(switch.listenPort) + '> data/dump-ports.txt')	
	print "Bye bye!"
	net.stop()

def prepareJava():
	_exec('mvn clean install')

if __name__ == '__main__':
	setLogLevel('info')
	prepareJava()
	for site in sites.keys():
		for i in num_clients:
			experimentId = site + '-' + str(i)
			experiment(site,sites[site],i)
