# Henson 

A project for on-device Android app debugging

## The name

The name Henson comes from [Jim Henson](https://en.wikipedia.org/wiki/Jim_Henson), best known for creating [The Muppets](https://en.wikipedia.org/wiki/The_Muppets). He was a puppeteer, and this project is about controlling other applications, hence "Henson".

## Usage

 - Henson app discovers applications based on querying for Services named ``
 - Henson application always shows configurable apps
 - Henson application requires signature to interact with debuggable app, uses signature to sign requests
   - Can key expose options/option-sets (e.g. in metadata of signed payload)?
   - Debuggable app must approve/deny signed requests from Henson app (has public key, can validate public key'd request)
   - Debuggable app doesn't need to validate calling app, just the request
 - Signature needs to be stored securely (how?)

## Debuggable app (receiver)

 - Exposes configurable components based on initial signed request for "features" from Henson app (via Broadcast)

## Private key

 - A private key will be created for any app the user wants to manage
 - A public key will be provided for the user, and she shall put that private key either in a remote file (preferable for dynamicity) or manually add the signature at onCreate of Application

## Feature types

 - Button
 - Dropdown (enum)
 - Text input (raw, autocomplete, possibly w/ options for input type (e.g. email, phone))
 - Checks/switches
 - Log reader
