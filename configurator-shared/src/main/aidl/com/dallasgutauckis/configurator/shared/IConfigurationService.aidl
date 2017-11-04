// IConfigurationService.aidl
package com.dallasgutauckis.configurator.shared;

interface IConfigurationService {
    /**
     * Returns a status code represented by MessageResponseCode
     */
    int onMessage(in byte[] publicKey, in byte[] jsonPayload, in byte[] signature);
}
