git config user.name "HulbertAlejandro"
git config user.email "hulberta.arangof@uqvirtual.edu.co"
ssh -T git@github.com
ls ~/.ssh/
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
ssh-add -l
ssh -T git@github.com
cat ~/.ssh/id_ed25519.pub
ssh -T git@github.com
git config user.name
git config user.email

