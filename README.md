# BlackBox
A log management SpringBoot app to learn about NoSQL using MongoDB. 

# Commands

## Event and User generator command 

`.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=generate"` 

## API command 

`.\mvnw.cmd spring-boot:run`

## Explain measure command

`mongosh blackbox --file docs\measures\funnel-explain.js`
