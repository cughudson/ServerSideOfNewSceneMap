git pull origin master
echo  "reload success"
echo  "staring package"
./mvnw  clean package -Dmaven.test.skip=true
echo "end package"

# java -jar /home/java-code/ServerSideOfNewSceneMap/map/target/cscec-0.0.1-SNAPSHOT.jar & 
java -jar /home/java-code/ServerSideOfNewSceneMap/hudson-map/target/cscec-0.0.1-SNAPSHOT.jar & 

