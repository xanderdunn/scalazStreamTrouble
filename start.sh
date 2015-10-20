# This convenience script is put into the Docker image so that multiple commands can be run easily
rabbitmq-server -detached
sleep 4
/scalaz-rabbitmq-bug/bin/scalaz-rabbitmq-bug
