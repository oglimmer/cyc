VAGRANT=YES
SSHUSER=vagrant
SSHSUDO="--sudo"

echo "For COUCHDB_PASSWORD=$COUCHDB_PASSWORD"
if [ -z "$COUCHDB_PASSWORD" ]; then
	echo "No Couchdb password. will exit."
	exit 1
fi

echo "For ENGINE_PASSWORD=$ENGINE_PASSWORD"
if [ -z "$ENGINE_PASSWORD" ]; then
	echo "No Engine password. will exit."
	exit 1
fi

if [ ! -e ~/.ssh/id_rsa.pub ]; then
	echo "You need to have a id_rsa in your ~/.ssh. (use ssh-keygen to generate one)"
	exit 1
fi
