import { WebPlugin } from '@capacitor/core';

import type { SmsUserConsentPlugin } from './definitions';

export class SmsUserConsentWeb
  extends WebPlugin
  implements SmsUserConsentPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
