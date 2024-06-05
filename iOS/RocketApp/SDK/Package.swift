// swift-tools-version: 5.7
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SDK",
    defaultLocalization: "cs",
    platforms: [.iOS(.v16)],
    products: [
        .library(
            name: "Shared",
            targets: ["Shared"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            path: "../../../shared/rocket/build/XCFrameworks/lib/Shared.xcframework"
        )
    ]
)
