
export abstract class AbstractHttpInterceptor {
  protected isWhiteListUrl(url: string): boolean {
    return url.includes('verify') ||
      url.includes('login') ||
      url.includes('register') ||
      url.includes('refresh') ||
      url.includes('reset-password');
  }
}
