# Basic Spring Boot Scraper
App to scrape pepper information from [ChilePlanet](http://www.chileplanet.eu/database.html)

### Table of Contents
1. [Installation and Usage](#Installation-and-Usage)
2. [Dependencies](#Dependencies)
3. [Notes](#Notes)


### **Installation and Usage**
Clone the repository
```bash
$ git clone https://github.com/connellboyce/spring-boot-scraper.git
```

Change directory to spring-boot-scraper
```bash
$ cd spring-boot-scraper
```

Clean and build the project with Maven
```bash
$ mvn clean
```
```bash
$ mvn install
```

Run the project through Maven and Spring Boot
```bash
$ mvn spring-boot:run
```


### **Dependencies**
* Spring Boot Starter
* Spring Boot Devtools
* Spring Boot Starter Test
* JUnit Vintage
* JSoup
* Apache Commons Lang3
* Jackson Mapper ASL


### **Notes**
* Because we had to process ~170 inputs, I wanted to expedite the process by using multiple threads with an ExecutorService.
    * Used 25 threads and cut the time by almost 4 times