### Install Dependencies on Mac
- `brew cask install dockertoolbox`
- `brew install rabbitmq-server`

### Run Locally
- `gradle installDist`
- `./build/install/scalaz-rabbitmq-bug/bin/scalaz-rabbitmq-bug`
- Output I see:

```
admin$ ./build/install/scalaz-rabbitmq-bug/bin/scalaz-rabbitmq-bug
11:21:43.667 [pool-3-thread-1] INFO  scalazproblem.Main$ - Sending Message1
11:21:43.672 [Thread-0] INFO  scalazproblem.Main$ - Received message Message1
11:21:44.690 [pool-3-thread-2] INFO  scalazproblem.Main$ - Sending Message2
11:21:44.692 [Thread-0] INFO  scalazproblem.Main$ - Received message Message2
11:21:45.693 [pool-3-thread-3] INFO  scalazproblem.Main$ - Sending Message3
11:21:45.694 [Thread-0] INFO  scalazproblem.Main$ - Received message Message3
11:21:46.694 [pool-3-thread-4] INFO  scalazproblem.Main$ - Sending Message4
11:21:46.695 [Thread-0] INFO  scalazproblem.Main$ - Received message Message4
11:21:46.695 [Thread-0] INFO  scalazproblem.Main$ - Cleaning up and ending.
<application terminates>
```

### Run in Docker Image (Ubuntu 14.04)
- `docker build --tag=$USER/scalaztest --file=./Dockerfile .`
- `docker run --rm $USER/scalaztest bash /start.sh`
- Output I see:

```
admin$ docker run --rm $USER/scalaztest bash /start.sh
Warning: PID file not written; -detached was passed.
18:17:26.127 [pool-3-thread-1] INFO  scalazproblem.Main$ - Sending Message1
18:17:26.154 [Thread-0] INFO  scalazproblem.Main$ - Received message Message1
18:17:27.161 [pool-3-thread-1] INFO  scalazproblem.Main$ - Sending Message2
18:17:27.163 [Thread-0] INFO  scalazproblem.Main$ - Received message Message2
18:17:28.165 [pool-3-thread-1] INFO  scalazproblem.Main$ - Sending Message3
18:17:28.168 [Thread-0] INFO  scalazproblem.Main$ - Received message Message3
18:17:29.170 [pool-3-thread-1] INFO  scalazproblem.Main$ - Sending Message4
18:17:29.172 [Thread-0] INFO  scalazproblem.Main$ - Received message Message4
18:17:29.172 [Thread-0] INFO  scalazproblem.Main$ - Cleaning up and ending.
<application terminates>
```
