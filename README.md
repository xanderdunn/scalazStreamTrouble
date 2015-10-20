### Install Dependencies on Mac
- `brew cask install dockertoolbox`
- `brew install rabbitmq-server`

### Run Locally (On Mac)
- `gradle installDist`
- `./build/install/scalaz-rabbitmq-bug/bin/scalaz-rabbitmq-bug`
- Output I see:

```
ip-10-24-0-108:scalaz-rabbitmq-bug admin$ ./build/install/scalaz-rabbitmq-bug/bin/scalaz-rabbitmq-bug
13:37:41.457 [pool-3-thread-6] INFO  scalazproblem.Main$ - Waiting to receive a message...
13:37:42.410 [pool-3-thread-7] INFO  scalazproblem.Main$ - Sending Message1
13:37:42.415 [pool-3-thread-8] INFO  scalazproblem.Main$ - Received message Message1
13:37:42.426 [pool-3-thread-3] INFO  scalazproblem.Main$ - Waiting to receive a message...
13:37:43.431 [pool-3-thread-1] INFO  scalazproblem.Main$ - Sending Message2
13:37:43.433 [pool-3-thread-8] INFO  scalazproblem.Main$ - Received message Message2
13:37:43.433 [pool-3-thread-6] INFO  scalazproblem.Main$ - Waiting to receive a message...
13:37:44.437 [pool-3-thread-2] INFO  scalazproblem.Main$ - Sending Message3
13:37:44.440 [pool-3-thread-7] INFO  scalazproblem.Main$ - Received message Message3
13:37:44.441 [pool-3-thread-2] INFO  scalazproblem.Main$ - Waiting to receive a message...
13:37:45.442 [pool-3-thread-5] INFO  scalazproblem.Main$ - Sending Message4
13:37:45.444 [pool-3-thread-3] INFO  scalazproblem.Main$ - Received message Message4
13:37:45.450 [pool-3-thread-8] INFO  scalazproblem.Main$ - Cleaning up and ending.
<application terminates>
```

### Run in Docker Image (Ubuntu 14.04)
- `docker build --tag=$USER/scalaztest --file=./Dockerfile .`
- `docker run --rm $USER/scalaztest bash /start.sh`
- Output I see:

```
ip-10-24-0-108:scalaz-rabbitmq-bug admin$ docker run --rm $USER/scalaztest bash /start.sh
Warning: PID file not written; -detached was passed.
20:34:27.989 [pool-3-thread-1] INFO  scalazproblem.Main$ - Waiting to receive a message...
<application hangs endlessly>
```
