package com.eden.orchid.wiki.model

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.pages.WikiSectionsPage
import javax.inject.Singleton

@Singleton
class WikiModel {

    var sectionsPage: WikiSectionsPage? = null

    var sections: MutableMap<String, WikiSection> = LinkedHashMap()

    val allPages: List<OrchidPage>
        get() {
            val allPages = ArrayList<OrchidPage>()
            for (value in sections.values) {
                allPages.add(value.summaryPage)
                allPages.addAll(value.wikiPages)
                if(value.bookPage != null) {
                    allPages.add(value.bookPage!!)
                }
            }
            if(sectionsPage != null) {
                allPages.add(sectionsPage!!)
            }

            return allPages
        }

    fun initialize(sections: List<WikiSection>) {
        this.sections = linkedMapOf(*(sections.map { it.key to it }.toTypedArray()))
    }

    fun getSection(sectionKey: String) : WikiSection? {
        if(sectionKey.isNotBlank()) {
            return sections.getOrDefault(sectionKey, null)
        }

        return null
    }
}
