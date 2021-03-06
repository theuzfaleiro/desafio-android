package dev.theuzfaleiro.trendingongithub.ui.feature.pullrequest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.theuzfaleiro.trendingongithub.ui.feature.pullrequest.model.data.PullRequest
import dev.theuzfaleiro.trendingongithub.ui.feature.pullrequest.model.data.User
import dev.theuzfaleiro.trendingongithub.ui.feature.pullrequest.repository.PullRequestRepository
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

private const val INFORMATION = 1
private const val ERROR = 2

class PullRequestViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    private val pullRequestRepository = mockk<PullRequestRepository>()

    private lateinit var pullRequestViewModel: PullRequestViewModel

    @Before
    fun setUp() {
        pullRequestViewModel = PullRequestViewModel(pullRequestRepository)
    }

    @Test
    fun shouldDisplayOpenPullRequests_WhenFetchOpenPullRequests() {
        //arrange
        every {
            pullRequestRepository.fetchPullRequests("theuzfaleiro", "minimal-weather")
        } returns Single.just(
            listOf(
                PullRequest(
                    "https://bit.ly",
                    42,
                    "First Pull Request Title",
                    "First Pull Request Description",
                    "28/01/2020",
                    User("theuzfaleiro", "https://bit.ly")
                )
            )
        )

        //act
        pullRequestViewModel.fetchPullRequests("theuzfaleiro", "minimal-weather")

        //assert
        pullRequestViewModel.getLoading().value shouldBe INFORMATION

        requireNotNull(pullRequestViewModel.getPullRequests().value).first().title shouldBe
                "First Pull Request Title"

    }

    @Test
    fun shouldDisplayAnError_WhenNoPullRequestsWereFetched() {
        //arrange
        every {
            pullRequestRepository.fetchPullRequests("theuzfaleiro", "minimal-weather")
        } returns Single.error(Throwable())

        //act
        pullRequestViewModel.fetchPullRequests("theuzfaleiro", "minimal-weather")

        //assert
        pullRequestViewModel.getPullRequests().value shouldBe null
        pullRequestViewModel.getLoading().value shouldBe ERROR
    }
}