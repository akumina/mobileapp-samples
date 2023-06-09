// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#if !EXCLUDE_FROM_MSALCPP

@class MSIDExternalSSOContext;

#import "MSIDAADAuthorizationCodeRequest.h"

@interface MSIDAADV1AuthorizationCodeRequest : MSIDAADAuthorizationCodeRequest

- (instancetype _Nullable )initWithEndpoint:(nonnull NSURL *)endpoint
                                   clientId:(nonnull NSString *)clientId
                                redirectUri:(nonnull NSString *)redirectUri
                                      scope:(nullable NSString *)scope
                                  loginHint:(nullable NSString *)loginHint
                                   resource:(nonnull NSString *)resource
                                 ssoContext:(nullable MSIDExternalSSOContext *)ssoContext
                                    context:(nullable id<MSIDRequestContext>)context NS_DESIGNATED_INITIALIZER;

- (instancetype _Nullable )initWithEndpoint:(nonnull NSURL *)endpoint
                                   clientId:(nonnull NSString *)clientId
                                redirectUri:(nonnull NSString *)redirectUri
                                      scope:(nullable NSString *)scope
                                  loginHint:(nullable NSString *)loginHint
                                 ssoContext:(nullable MSIDExternalSSOContext *)ssoContext
                                    context:(nullable id<MSIDRequestContext>)context NS_UNAVAILABLE;

@end

#endif
