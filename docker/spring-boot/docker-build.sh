echo "-------------------- spring-boot-template-reactive - docker build --------------------"
BASEDIR=$(dirname "$0")
PROJECT_ROOT_DIR="$BASEDIR/../.."
cd $PROJECT_ROOT_DIR

VERSION="$(basename -s .jar build/libs/*.jar)"
VERSION="${VERSION//spring-boot-template-reactive-/}"

docker build -f docker/spring-boot/Dockerfile-dev -t dptablo/spring-boot-template-reactive:$VERSION .
docker save -o build/docker-dptablo-spring-boot-template-reactive-$VERSION.tar dptablo/spring-boot-template-reactive:$VERSION

echo "created docker image 'dptablo/spring-boot-template-reactive:$VERSION'"
echo "-----------------------------------------------------------------------------"