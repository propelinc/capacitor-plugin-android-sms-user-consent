import { WebPlugin } from '@capacitor/core';

import type { SmsUserConsentPlugin } from './definitions';

export class SmsUserConsentWeb
  extends WebPlugin
  implements SmsUserConsentPlugin {
  async subscribeToOTP(): Promise<string> {
    console.log('ECHO');
    return '';
  }
}
