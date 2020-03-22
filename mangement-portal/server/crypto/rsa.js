const path = require('path')
const rsa = {}
const fs = require('fs')
const NodeRSA = require('node-rsa')
const crypto = require('crypto')

rsa.initLoadServerKeys = (basePath) => {
    rsa.serverPub = fs.readFileSync(path.resolve(basePath, 'keys', 'server.public.pem'));
    rsaWrapper.serverPrivate = fs.readFileSync(path.resolve(basePath, 'keys', 'server.private.pem'));
}

rsa.generate = (direction)=> {
    let key = new NodeRSA();
    key.generateKeyPair(2048, 65537);
    fs.writeFileSync(path.resolve(__dirname, '../keys', direction + '.private.pem'), key.exportKey('pkcs8-private-pem'));
    fs.writeFileSync(path.resolve(__dirname, '../keys', direction + '.public.pem'), key.exportKey('pkcs8-public-pem'));
    return true;
}

rsa.encrypt = (publicKey, message) => {
    let encoded = crypto.publicEncrypt({
        key: publicKey,
        padding: crypto.RSA_PKCS1_OAEP_PADDING
    }, Buffer.from(message));
    return encoded.toString('base64');
}

rsa.decrypt = (privateKey, message) => {
    let encoded = crypto.privateDecrypt({
        key: privateKey,
        padding: crypto.RSA_PKCS1_OAEP_PADDING
    }, Buffer.from(message, 'base64'));
    return encoded.toString();
}

module.exports = rsa;