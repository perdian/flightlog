#!/bin/bash
if [[ "$1" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    if echo $(git tag) | grep "release-$1" > /dev/null; then
        echo "Version '$1' has already been used!"
        exit 1
    fi

    echo "Tagging release version: $1"
    RELEASE_VERSION=$1
    RELEASE_MAJOR_MINOR_VERSION=$(echo $1 | cut -f -2 -d '.')
    RELEASE_PATCH_VERSION=$(echo $1 | cut -f 3- -d '.')
    NEXT_VERSION="$RELEASE_MAJOR_MINOR_VERSION.$(expr $RELEASE_PATCH_VERSION + 1)"

    mvn versions:set -DnewVersion=$RELEASE_VERSION
    mvn clean package
    git commit -am "Release version $RELEASE_VERSION"
    git tag -f release-$RELEASE_VERSION
    mvn versions:set -DnewVersion=$NEXT_VERSION-snapshot
    git commit -am "Prepare next version"
    mvn versions:commit

else
    echo "Invalid version: $1"
    exit 1
fi