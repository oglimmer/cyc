#!/bin/bash

docker run --rm --network=isolated_nw --volumes-from cyc-web --volumes-from cyc-engine codeyourrestaunt/setup
