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
#import "MSIDAuthenticationScheme.h"

@implementation MSIDTokenRequest

- (instancetype)initWithEndpoint:(NSURL *)endpoint
                      authScheme:(MSIDAuthenticationScheme *)authScheme
                        clientId:(NSString *)clientId
                           scope:(NSString *)scope
                      ssoContext:(MSIDExternalSSOContext *)ssoContext
                         context:(nullable id<MSIDRequestContext>)context
{
    self = [super init];
    if (self)
    {
        NSParameterAssert(clientId);
        NSParameterAssert(endpoint);
        
        self.context = context;
        self.externalSSOContext = ssoContext;
        
        NSMutableDictionary *parameters = [NSMutableDictionary new];
        parameters[MSID_OAUTH2_CLIENT_ID] = clientId;
        parameters[MSID_OAUTH2_SCOPE] = scope;
        
        NSDictionary *authHeaders = authScheme.schemeParameters;
        if ([authHeaders count] > 0)
        {
            [parameters addEntriesFromDictionary:authHeaders];
        }
        
        _parameters = parameters;
        
        NSMutableURLRequest *urlRequest = [NSMutableURLRequest new];
        urlRequest.URL = endpoint;
        urlRequest.HTTPMethod = @"POST";
        _urlRequest = urlRequest;
    }
    
    return self;
}

@end

#endif
