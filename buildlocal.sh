./gradlew clean build
mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)
docker build --build-arg DEPENDENCY=build/dependency -t 192.168.1.210:32000/customuserobjects:test .
docker run -p 8081:8081 -t 192.168.1.210:32000/customuserobjects:test
