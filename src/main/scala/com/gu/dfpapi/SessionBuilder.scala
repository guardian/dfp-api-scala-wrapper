package com.gu.dfpapi

import com.google.api.ads.common.lib.auth.OfflineCredentials
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api.DFP
import com.google.api.ads.dfp.lib.client.DfpSession
import org.apache.commons.configuration.Configuration

object SessionBuilder {

  def buildSessionFromFile(): DfpSession = {
    val credential = new OfflineCredentials.Builder()
      .forApi(DFP)
      .fromFile()
      .build()
      .generateCredential()
    new DfpSession.Builder()
      .withOAuth2Credential(credential)
      .fromFile()
      .build()
  }

  def buildSessionFromConfig(config: Configuration): DfpSession = {
    val credential = new OfflineCredentials.Builder()
      .forApi(DFP)
      .from(config)
      .build()
      .generateCredential()
    new DfpSession.Builder()
      .withOAuth2Credential(credential)
      .from(config)
      .build()
  }
}
