//
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

#import "MSIDCIAMAuthorityResolver.h"
#import "MSIDCIAMAuthority.h"
#import "MSIDAADNetworkConfiguration.h"
#import "MSIDAADEndpointProviding.h"

@implementation MSIDCIAMAuthorityResolver

- (void)resolveAuthority:(MSIDCIAMAuthority *)authority
       userPrincipalName:(__unused NSString *)upn
                validate:(BOOL)validate
                 context:(id<MSIDRequestContext>)context
         completionBlock:(MSIDAuthorityInfoBlock)completionBlock
{
    
    NSParameterAssert([authority isKindOfClass:MSIDCIAMAuthority.self]);
    
    if (validate && ![authority isKnown])
    {
        __auto_type error = MSIDCreateError(MSIDErrorDomain, MSIDErrorUnsupportedFunctionality, @"Authority validation is not supported for this type of authority", nil, nil, nil, context.correlationId, nil, YES);
        if (completionBlock) completionBlock(nil, NO, error);
        return;
    }
    
    __auto_type endpoint = [MSIDAADNetworkConfiguration.defaultConfiguration.endpointProvider openIdConfigurationEndpointWithUrl:authority.url];
    
    if (completionBlock) completionBlock(endpoint, validate, nil);
}

@end

#endif

