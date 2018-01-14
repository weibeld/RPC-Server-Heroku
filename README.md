# Heroku RabbitMQ Synchronous Server

Synchronous (RPC-like) communication with [RabbitMQ](http://www.rabbitmq.com/).

This is a server handling synchronous request from a client. That means, after sending the request, the client blocks until it receives the response from the server.

The request sent by the client is a string, and the response returned by the server is also a string.

The synchronous communication is implemented with RabbitMQ's remote procedure call (RPC) facilities. 

The corresponding synchronous client can be found [here](https://github.com/weibeld/Syn-Client-Heroku).

## Related Example

Note that although this code uses RabbitMQ's RPC mechanism, it does not implement a true RPC (calling a remote method) communication.

A true RPC implementation with RabbitMQ using JSON-RPC can be found [here (client)](https://github.com/weibeld/JSON-Syn-Client-Heroku) and [here (server)](https://github.com/weibeld/JSON-RPC-Server-Heroku).

## Implementation

This implementation uses:

- [RabbitMQ](http://www.rabbitmq.com/): message passing service (message broker) implementing the [AMQP](https://www.amqp.org/) protocol
- [RabbitMQ Java Client Library](http://www.rabbitmq.com/java-client.html): Java APIs for RabbitMQ
- [`StringRpcServer`](http://www.rabbitmq.com/releases/rabbitmq-java-client/current-javadoc/com/rabbitmq/client/StringRpcServer.html): class of the RabbitMQ Java Client Library for implementing an RPC server handling string requests
- [Heroku](http://heroku.com): Platform as a Service (PaaS) provider for running any apps in the cloud
- [CloudAMQP](https://elements.heroku.com/addons/cloudamqp): Heroku add-on providing "RabbitMQ as a Service" for Heroku apps


## Run on Heroku

### Create Heroku App

Create an app on Heroku for your server:

~~~bash
heroku create YOUR-APP-NAME
~~~

### Set Up RabbitMQ

Install the CloudAMQP add-on for this Heroku application:

~~~bash
heroku addons:create cloudamqp
~~~

This creates an additional Heroku dyno running a **RabbitMQ server** for your application on Heroku.

In addition, it adds the following config vars to the client Heroku application:

- `CLOUDAMQP_APIKEY`
- `CLOUDAMQP_URL`

You can confirm this with `heroku config`.

The value of the `CLOUDAMQP_URL` variable is the URI of the RabbitMQ server that has just been created on Heroku. Your application needs this URI in order to connect to the RabbitMQ server.

**Important:** you have to execute the above command **only once** for the client/server pair. If you already ran this for the client, then **do not** run it again for the server. Instead, just add the above config vars to the server application:

~~~bash
heroku config:set CLOUDAMQP_APIKEY="..."
heroku config:set CLOUDAMQP_URL="..."
~~~

### Run

Deploy and run the server application on Heroku:

~~~bash
git push heroku master
~~~

### Monitor

To see the queues and their content on the RabbitMQ server, use the **CloudAMQP Dashboard**:

~~~bash
heroku addons:open cloudamqp
~~~

Note that this command only works from the application (client or server) on which you *installed* the CloudAMQP add-on (i.e. the one in which you executed `heroku addons:create cloudamqp`).

### Order of Execution

The client is a one-shot application. It makes one request to the server, waits for the response, and then terminates. 

The server is a long-running application. That is, once started, it is supposed to just run and never stop. The [client](https://github.com/weibeld/Syn-Client-Heroku), on the other hand, is a one-shot application that makes one request to the server and then terminates.

Thus, the normal order of execution is to first start the server, and then the client. In this case, the request sent by the client is handled immediately by the server.

However, starting the client before the server is running is also possible. In this case, there are two possibilities of what can happen:

- If the request queue already exists (if the server has been running before at some time), the message sent by the client is stored in this queue until the server starts up. When this happens, the message is delivered to the server and handled by it.
- If the request queue does not exist, then the message sent by the client is simply discarded. When the server starts up, it doesn't receive this message, because it has not been saved in the request queue. Consequently, the client will never receive a response for this message.


### Tip

If no messages seem to be sent at all, make sure that there's actually a dyno scaled for both the client and server:

~~~bash
heroku ps
~~~~

Scale one dyno for the server:

~~~bash
heroku ps:scale server=1
~~~

## Run Locally

During development, it is convenient to run the application locally instead of deploying it to Heroku:

However, for this to work, you need to install a RabbitMQ server on your machine.

### Install RabbitMQ Server

Install the RabbitMQ server on your local machine according to the instructions [here](http://www.rabbitmq.com/download.html).

This provides the command `rabbitmq-server` for starting the RabbitMQ server on the default port 5672

If you installed with Homebrew, you might need to add the folder containing the RabbitMQ executables to the `PATH`.

### Run

First, make sure the RabbitMQ server is running on the local machine with `rabbitmq-server`.

Then, start the server application:

~~~bash
heroku local
~~~~

### Monitor

See all queues and their content of the local RabbitMQ server in the [Management Web UI](http://www.rabbitmq.com/management.html) here (username: **guest**, password: **guest**): <http://localhost:15672> .

You can also list all the queues from the command line:

~~~bash
sudo rabbitmqctl list_queues
~~~
