VAGRANT=YES
SSHUSER=vagrant
SSHSUDO="--sudo"

echo "For COUCHDB_PASSWORD=$COUCHDB_PASSWORD"
if [ "$COUCHDB_PASSWORD" = "" ]; then
	echo "No Couchdb password. will exit."
	exit 1
fi
