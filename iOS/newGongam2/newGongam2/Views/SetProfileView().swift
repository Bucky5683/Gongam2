//
//  SetProfileView().swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI

struct SetProfileView: View {
    @EnvironmentObject var userData: UserData
    
    var body: some View {
        VStack{
            Button{
                print("Tapped!")
            } label: {
                Image(userData.profileImageURL)
            }
        }
    }
}

#Preview {
    SetProfileView()
}
