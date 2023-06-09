// Copyright (c) Microsoft Corporation.
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

#import "MSIDTokenRequest.h"
#import "MSIDThumbprintCalculatable.h"

@class MSIDAuthenticationScheme;

@interface MSIDRefreshTokenGrantRequest : MSIDTokenRequest <MSIDThumbprintCalculatable>

- (instancetype _Nullable)initWithEndpoint:(nonnull NSURL *)endpoint
                                authScheme:(nonnull MSIDAuthenticationScheme *)authScheme
                                  clientId:(nonnull NSString *)clientId
                                     scope:(nullable NSString *)scope
                              refreshToken:(nonnull NSString *)refreshToken
                               redirectUri:(nullable NSString *)redirectUri
                           extraParameters:(nullable NSDictionary *)extraParameters
                                ssoContext:(nullable MSIDExternalSSOContext *)ssoContext
                                   context:(nullable id<MSIDRequestContext>)context NS_DESIGNATED_INITIALIZER;

- (instancetype _Nullable)initWithEndpoint:(nonnull NSURL *)endpoint
                                authScheme:(nonnull MSIDAuthenticationScheme *)authScheme
                                  clientId:(nonnull NSString *)clientId
                                     scope:(nullable NSString *)scope
                                ssoContext:(nullable MSIDExternalSSOContext *)ssoContext
                                   context:(nullable id<MSIDRequestContext>)context NS_UNAVAILABLE;

@end

#endif
