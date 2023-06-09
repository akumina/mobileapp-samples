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

#import "NSError+MSIDExtensions.h"
#import "MSIDErrorConverter.h"

@implementation NSError (MSIDExtensions)

- (NSError *)msidErrorWithFilteringOptions:(MSIDErrorFilteringOptions)option
{
    NSMutableDictionary *errorUserInfo = [self.userInfo mutableCopy];
    
    if (option & MSIDErrorFilteringOptionRemoveUrlParameters)
    {
        // Don't put raw url in NSError because it can contain sensitive data.
        NSURL *failedUrl = errorUserInfo[NSURLErrorFailingURLErrorKey];
        [errorUserInfo removeObjectForKey:NSURLErrorFailingURLErrorKey];
        [errorUserInfo removeObjectForKey:NSURLErrorFailingURLStringErrorKey];
        
        if (failedUrl)
        {
            // Remove parameters from failed url.
            NSURLComponents *components = [NSURLComponents componentsWithURL:failedUrl resolvingAgainstBaseURL:YES];
            components.queryItems = nil;
            failedUrl = components.URL;
            
            errorUserInfo[NSURLErrorFailingURLErrorKey] = failedUrl;
            errorUserInfo[NSURLErrorFailingURLStringErrorKey] = failedUrl.absoluteString;
        }
    }
    
    __auto_type error = [[NSError alloc] initWithDomain:self.domain code:self.code userInfo:errorUserInfo];
    
    return error;
}

- (id<MSIDErrorConverting>)msidErrorConverter
{
    id<MSIDErrorConverting> errorConverter = MSIDErrorConverter.errorConverter;

    if (!errorConverter)
    {
        errorConverter = MSIDErrorConverter.defaultErrorConverter;
    }

    return errorConverter;
}

- (nullable NSString *)msidOauthError
{
    return self.userInfo[self.msidErrorConverter.oauthErrorKey];
}

- (nullable NSString *)msidSubError
{
    return self.userInfo[self.msidErrorConverter.subErrorKey];
}

- (nullable NSDictionary *)msidGetStatusCodeUserInfoFromUnderlyingErrorCode
{
    NSMutableDictionary *additionalUserInfo = nil;
    if (self.userInfo[NSUnderlyingErrorKey] != nil)
    {
        additionalUserInfo = [NSMutableDictionary new];
        NSError *underlyingError = self.userInfo[NSUnderlyingErrorKey];
        [additionalUserInfo setValue:@(underlyingError.code) forKey:@"statusCode"];
    }

    return additionalUserInfo;
}

@end
