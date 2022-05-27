export interface SmsUserConsentPlugin {
  subscribeToOTP(): Promise<string>;
}
