# Console

A project for on-device Android app debugging

## Usage

 - Console app discovers applications based on querying for Activities that have action `"com.dallasgutauckis.console.ACTION_CONFIGURE"`
 - Console application always shows configurable apps
 - Console application requires signature to interact with debuggable app, uses signature to sign requests
   - Can key expose options/option-sets (e.g. in metadata of signed payload)?
   - Debuggable app must approve/deny signed requests from Console app (has public key, can validate public key'd request)
   - Debuggable app doesn't need to validate calling app, just the request
 - Signature needs to be stored securely (how?)

## Debuggable app

 - Exposes configurable components based on initial signed request for "features" from Console app (via Broadcast)

## Private key

 - Private key should be created by Debuggable app creator, kept under lock and key, only used to create new keys for new debuggers (don't share private key)

## Feature types

 - Button
 - Dropdown (enum)
 - Text input (raw, autocomplete, possibly w/ options for input type (e.g. email, phone))
 - Checks/switches
 - Log reader
