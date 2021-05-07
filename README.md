# Project streaming data from Twitter in Kafka

## Project Modules
### App-data-config: 
Responsible for providing the data for all the configurations of the project. Configuration for Kafka, Kafka Admin, Kafka Producer, Twitter Data Filters and Retry.

### Common-config:
Sets the retry configuration using retry data from app-data-config.

### Docker Compose
To run kafka brokers and zookeeper from the images.

### Kafka
* kafka-admin: Set the kafka configuration using data from app-data-config.
* kafka-model: Generates the kafka model using avro library.
* kafka-producer: Configures and creates Kafka Template for serializing and sending data to brokers.

### Twitter Kafka
Main module which uses other module for getting streamed data from Twitter to Kafka
* Data for different configurations is passed in properties files which is read by other modules.

