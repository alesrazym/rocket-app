import Combine
import Dependencies
@testable import Networking
@testable import RocketsClient
import XCTest

final class RocketsClientLiveAsyncTests: XCTestCase {
  private func setNetworkClient(requester: URLRequester) -> NetworkClient {
      NetworkClient(
        urlRequester: requester,
        networkMonitorClient: .live(onQueue: .main)
      )
  }

  func test_rocket_request_success() async {
    var rocketData: RocketDetail?

    // swiftlint:disable:next force_try
    let dataMock = try! JSONEncoder().encode(RocketDetailDTO.mock)
    let responseMock = HTTPURLResponse(
      url: URL(string: "www.google.com")!,
      statusCode: 200,
      httpVersion: nil,
      headerFields: [:]
    )!
    let requesterMock = URLRequester { _ in
      Just((dataMock, responseMock))
        .setFailureType(to: URLError.self)
        .eraseToAnyPublisher()
    }

    await withDependencies {
      $0.networkClientType = setNetworkClient(requester: requesterMock)
      $0.lineMeasureConverter = .live
      $0.weightScaleConverter = .live
      $0.stageConverter = .live
      $0.rocketConverter = .live
    } operation: {
      do {
        rocketData = try await RocketsClient.liveAsync.getRocket("")
      } catch let error {
        XCTFail("Unexpected failure. \(error)")
      }

      XCTAssertEqual(rocketData?.id, "apollo13")
    }
  }

  func test_rocket_request_failure() async {
    let requesterMock = URLRequester { _ in
      Fail(error: URLError(.badServerResponse))
        .eraseToAnyPublisher()
    }

    await withDependencies {
      $0.networkClientType = setNetworkClient(requester: requesterMock)
    } operation: {
      do {
        _ = try await RocketsClient.liveAsync.getRocket("")
      } catch let error {
        guard error is RocketsClientAsyncError else {
          XCTFail("Unexpected error type - \(error).")
          return
        }
      }
    }
  }

  func test_rockets_request_success() async {
    var rocketData: [RocketDetail] = []

    // swiftlint:disable:next force_try
    let dataMock = try! JSONEncoder().encode([RocketDetailDTO.mock])
    let responseMock = HTTPURLResponse(
      url: URL(string: "www.google.com")!,
      statusCode: 200,
      httpVersion: nil,
      headerFields: [:]
    )!
    let requesterMock = URLRequester { _ in
      Just((dataMock, responseMock))
        .setFailureType(to: URLError.self)
        .eraseToAnyPublisher()
    }

    await withDependencies {
      $0.networkClientType = setNetworkClient(requester: requesterMock)
      $0.lineMeasureConverter = .live
      $0.weightScaleConverter = .live
      $0.stageConverter = .live
      $0.rocketConverter = .live
      $0.rocketsConverter = .live
    } operation: {
      do {
        rocketData = try await RocketsClient.liveAsync.getAllRockets()
      } catch let error {
        XCTFail("Unexpected failure. \(error)")
      }

      XCTAssertEqual(rocketData[0].id, "apollo13")
    }
  }

  func test_rockets_request_failure() async {
    let exp = expectation(description: "")

    let requesterMock = URLRequester { _ in
      Fail(error: URLError(.badServerResponse))
        .eraseToAnyPublisher()
    }

    await withDependencies {
      $0.networkClientType = setNetworkClient(requester: requesterMock)
    } operation: {
      do {
        _ = try await RocketsClient.liveAsync.getAllRockets()
      } catch let error {
        guard error is RocketsClientAsyncError else {
          XCTFail("Unexpected error type - \(error).")
          return
        }

        exp.fulfill()
      }

        await fulfillment(of: [exp], timeout: 0.1)
    }
  }
}
