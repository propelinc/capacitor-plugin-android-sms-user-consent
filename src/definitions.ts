export interface SmsUserConsentPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
