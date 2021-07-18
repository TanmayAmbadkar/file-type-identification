/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.tivi.interactors

import app.tivi.data.repositories.episodes.SeasonsEpisodesRepository
import app.tivi.data.repositories.followedshows.FollowedShowsRepository
import app.tivi.interactors.UpdateShowSeasonData.Params
import app.tivi.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateShowSeasonData @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val seasonsEpisodesRepository: SeasonsEpisodesRepository,
    private val followedShowsRepository: FollowedShowsRepository
) : Interactor<Params> {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun invoke(params: Params) {
        if (followedShowsRepository.isShowFollowed(params.showId)) {
            // Then update the seasons/episodes
            if (params.forceRefresh || seasonsEpisodesRepository.needShowSeasonsUpdate(params.showId)) {
                seasonsEpisodesRepository.updateSeasonsEpisodes(params.showId)
            }
            // Finally update any watched progress
            if (params.forceRefresh || seasonsEpisodesRepository.needShowEpisodeWatchesSync(params.showId)) {
                seasonsEpisodesRepository.syncEpisodeWatchesForShow(params.showId)
            }
        } else {
            seasonsEpisodesRepository.removeShowSeasonData(params.showId)
        }
    }

    data class Params(val showId: Long, val forceRefresh: Boolean)
}