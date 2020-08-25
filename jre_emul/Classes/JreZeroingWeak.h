// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//
//  JreZeroingWeak.h
//  JreEmulation
//
//  Created by Michał Pociecha-Łoś
//
// INTERNAL ONLY. For use by JRE emulation code.

#ifndef JRE_ZEROING_WEAK_H_
#define JRE_ZEROING_WEAK_H_

#if !__has_feature(objc_arc)

#import <Foundation/Foundation.h>

FOUNDATION_EXPORT id JreZeroingWeakGet(id zeroingWeak);
FOUNDATION_EXPORT id JreMakeZeroingWeak(id object);

#endif  // !__has_feature(objc_arc)

#endif  // JRE_ZEROING_WEAK_H_
