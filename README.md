# Trocaire Collect

[Demo Video](https://drive.google.com/file/d/1GjROvvjNGP5D_c4PnXZ93e18og4ZPNnX/view?usp=sharing)

Human rights should be universally recognised. Unfortunately many countries' governments fail to uphold the human rights of citizens. In some cases, governments actively limit citizens' human rights, and persecute those that advocate for them. NGOs like Trócaire, collect human rights abuse cases and offer support through trained Human Rights Defenders (HRD). These HRDs live within these communities, and collect data for Trócaire so issues can be brought to the right authorities. 

The current system which Trócaire uses is Commcare. Commcare is not suited for the remote style and highly sensitive information that is collected by Trócaire. They have experienced issues with: Commcare needing internet for device authentication, HRD training on the application UI, and security issues such as the app being easily identifiable and sensitive information being accessible if a device’s password is compromised. 

Our solution, Trócaire Collect, is designed to be discreet, secure, easy to use and available in areas with limited Internet access. The app is discreet, as it is camouflaged under the disguise of a fully functioning calculator. This protects HRDs that may be stopped and searched, hiding it from Government officials. The app is secure, as forms are encrypted before being saved, and it uses a hybrid of symmetric and asymmetric encryption for transmission to a backend database. The app is easy to use with a minimal and simple UI design. Its two tabbed layout reduces the time needed for training of tech inexperienced HRDs. The app works offline, saving encrypted forms until they can be delivered successfully, before discarding them for victim’s protection and packet sizes are smaller than 1 kB to reduce data usage. 

Ultimately we deliver a full stack data collection solution that can be extended to be used by other NGOs that need a secure and resilient way to collect information remotely.


## Running the project on your computer

1. Install Node
2. Install Dependencys
3. Run the project

### Installing Node

Node can be installed from the download page [here](https://nodejs.org/en/download/).

For instalation on windows follow this [guide](https://phoenixnap.com/kb/install-node-js-npm-on-windows)

### Installing Project Dependencies

Navigate into the server directory in the mangement-portal folder.

Using `npm install` all the dependices in package-lock.json will be downloaded

### Running the project

Once the project dependencies are installed the project can be run either through `node index.js` or with `nodemon index.js`.

> nodemon allows for hot refreshs with new file saves but is the same as node

Look at Aritecture.md for more information about the server architecture.
