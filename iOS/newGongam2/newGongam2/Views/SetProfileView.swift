//
//  SetProfileView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI

struct SetProfileView: View {
    @EnvironmentObject var userData: UserData
    
    var body: some View {
        NavigationView {
            VStack{
                Button{
                    print(userData.id)
                    print(userData.name)
                } label: {
                    AsyncImage(url: URL(string: userData.profileImageURL))
                }
                
                
            }
        }.navigationBarHidden(true)
    }
}

#Preview {
    SetProfileView()
}
