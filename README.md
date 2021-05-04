# Project streaming data from Twitter in Kafka

## Project Modules
### App-data-config: 
Responsible for setting the data for configurations related to Kafka, Twitter Data and Retry.

### Common-config:
Sets the retry configuration using retry data from app-data-config.

### Docker Compose
To run kafka brokers and zookeeper from the images.

### Kafka
* kafka-admin: Set the kafka configuration using data from app-data-config.
* kafka-model: Generates the kafka model using avro library.

### Twitter Kafka
Main module which uses other module for getting streamed data from Twitter to Kafka
* Data for different configurations is passed in properties files which is read by other modules.

