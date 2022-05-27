import { registerPlugin } from '@capacitor/core';

import type { SmsUserConsentPlugin } from './definitions';

const SmsUserConsent = registerPlugin<SmsUserConsentPlugin>('SmsUserConsent', {
  web: () => import('./web').then(m => new m.SmsUserConsentWeb()),
});

export * from './definitions';
export { SmsUserConsent };
