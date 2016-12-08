VAGRANT="${VAGRANT:-YES}"
SSHUSER=ubuntu
SSHSUDO="--sudo"

if [ ! -e ~/.ssh/id_rsa.pub ]; then
	echo "You need to have a id_rsa in your ~/.ssh. (use ssh-keygen to generate one)"
	exit 1
fi
