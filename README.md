# Ukwikora Inspector

Continuous monitoring of robot framework generating a static website

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisite

To be able to install Ukwikora Inspector, you will need to have Java JDK 1.8 installed and Maven installed on your machine.

The projects has two dependencies that will need to be installed manually:

* https://github.com/kabinja/ukwikora
* https://github.com/kabinja/gitlab-loader

Both dependencies can be installed using mvn clean install.

### Installing

1. Clone the project on your machine using git clone https://github.com/kabinja/ukwikora-inspector
2. Move to the directory
3. run mvn clean install

## Running the tests

1. Move to the directory
2. run mvn test