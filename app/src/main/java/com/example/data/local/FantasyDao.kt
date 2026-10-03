package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FantasyDao {

    @Query("SELECT * FROM user_teams WHERE matchId = :matchId ORDER BY createdAt DESC")
    fun getTeamsForMatch(matchId: String): Flow<List<TeamEntity>>

    @Query("SELECT * FROM user_teams ORDER BY createdAt DESC")
    fun getAllTeams(): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Query("SELECT * FROM user_teams WHERE id = :teamId LIMIT 1")
    suspend fun getTeamById(teamId: String): TeamEntity?

    @Query("UPDATE user_teams SET totalPoints = :points WHERE id = :teamId")
    suspend fun updateTeamPoints(teamId: String, points: Int)

    // Joined Contests
    @Query("SELECT * FROM joined_contests ORDER BY joinedAt DESC")
    fun getAllJoinedContests(): Flow<List<JoinedContestEntity>>

    @Query("SELECT * FROM joined_contests WHERE matchId = :matchId ORDER BY joinedAt DESC")
    fun getJoinedContestsForMatch(matchId: String): Flow<List<JoinedContestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoinedContest(contest: JoinedContestEntity)

    @Query("UPDATE joined_contests SET fantasyPoints = :points, currentRank = :rank WHERE id = :id")
    suspend fun updateContestProgress(id: String, points: Int, rank: Int)

    // Wallet
    @Query("SELECT * FROM user_wallet WHERE id = 1 LIMIT 1")
    fun getWallet(): Flow<WalletEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWallet(wallet: WalletEntity)

    // Transactions
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
}
