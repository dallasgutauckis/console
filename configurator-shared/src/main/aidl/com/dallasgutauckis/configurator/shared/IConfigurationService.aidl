// IConfigurationService.aidl
package com.dallasgutauckis.configurator.shared;

interface IConfigurationService {
    void onMessage(in byte[] publicKey, in byte[] jsonPayload, in byte[] signature);
}
