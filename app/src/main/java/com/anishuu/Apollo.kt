package com.anishuu

import com.apollographql.apollo.ApolloClient

val apolloClient: ApolloClient = ApolloClient.builder()
    .serverUrl("https://graphql.anilist.co/")
    .build()