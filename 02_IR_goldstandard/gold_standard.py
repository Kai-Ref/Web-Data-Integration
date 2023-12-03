import numpy as np
import pandas as pd
import Levenshtein



def leventstein_similarity(str1:str, str2:str):
    """
    Compares leventstein similarity between str1 and str2
    """
    return 1 - Levenshtein.distance(str1, str2) / max(len(str1), len(str2))




def same_similarity(str1:str, str2:str):
    """
    Compares whether str1 and str2 are equal
    """
    return str1==str2



def tokenize_date(date):
    # Tokenize the date string into year, month, and day
    return set(date.split('-'))

def date_similarity(date1, date2, threshold=1):
    
    if date1 is None or date2 is None:
        return 0
    
    tokens1 = tokenize_date(date1)
    tokens2 = tokenize_date(date2)
    
    intersection = len(tokens1.intersection(tokens2))
    union = len(tokens1.union(tokens2))
    jaccard_similarity = intersection / union
    
    # if jaccard_similarity >= threshold:
    #     return True
    # else:
    #     return False
    return jaccard_similarity



def get_similarity_matrix(similarity_function:callable, col1:pd.Series, col2:pd.Series) -> np.ndarray:
    """
    Computes a similarity matrix between each value of col1 and col2.
    similarity_function is a callable, i.e. leventstein_similarity, same_similarity, date_similarity
    """

    similarity_matrix = np.zeros((len(col1), len(col2)))

    for i, entry1 in enumerate(col1):
        for j, entry2 in enumerate(col2):

            similarity_matrix[i, j] = similarity_function(entry1, entry2) 
        # print(f"Row {i}")     

    return similarity_matrix





def create_result(df1:pd.DataFrame, df2:pd.DataFrame, similarity_matrix:np.ndarray, normalization:int=1) -> pd.DataFrame:
    """
    Creates a dataframe for each entry in df1 and the most similiar entry in df2. 
    """

    matches = pd.DataFrame()
    matches['df1'] = df1['id']
    matches['df2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['id'].values
    matches['name1'] = df1['name'].values
    matches['birthdate1'] = df1['birthdate'].values
    matches['name2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['name'].values
    matches['birthdate2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['birthdate'].values
    matches['nat1'] = df1['nationality']
    matches['nat2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['nationality'].values
    # matches['positions1'] = df1['positions']
    # matches['positions2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['positions'].values
    matches['club1'] = df1['club']
    matches['club2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['club'].values
    # matches['current_mv_1'] = df1['current_market_value']
    # matches['current_mv_2'] = df2.iloc[similarity_matrix.argmax(axis=1)]['current_market_value'].values
    matches['score'] = similarity_matrix.max(axis=1)/normalization
    
    return matches




def get_perfect_matches(
        result:pd.DataFrame, 
        perfect_match_at_least_threshhold:float
    ) -> pd.DataFrame:
    """
    Function takes result dataframe.
    Full Matches have a score of at least perfect_match_at_least_threshhold.
    A new column named 'match' containing True-values is added.
    """
    
    perfect_matches = result[result.score >= perfect_match_at_least_threshhold].copy()
    perfect_matches["match"] = True

    return perfect_matches


def get_corner_cases(
        result:pd.DataFrame, 
        perfect_match_at_least_threshhold:float, 
        corner_case_at_least_threshhold:float
    ) -> pd.DataFrame:
    """
    Function takes result dataframe.
    Corner Cases have a score between corner_case_at_least_threshhold and perfect_match_at_least_threshhold.
    A new column named 'match' containing None-values is added. This has to be set manually by comparing both entities.
    """
    
    corner_cases = result[(result.score >= corner_case_at_least_threshhold) & (result.score < perfect_match_at_least_threshhold)].copy()
    corner_cases["match"] = None

    return corner_cases


def get_non_matches(
        result:pd.DataFrame, 
        corner_case_at_least_threshhold:float,
        n:int=None
    ) -> pd.DataFrame:
    """
    I am not quite sure how to implement the function.

    I took all the matches with a higher score than corner_case_at_least_threshhold. 
    After that, I sampled them, took n random players and matched them to the non_matches.
    By doing this, I try to ensure that there is no match accidentally.
    """

    non_matches = result[result.score < corner_case_at_least_threshhold].sample(n=n, replace=True, random_state=42).copy()
    non_matches["match"] = False
    non_matches["score"] = None

    if n is None:
        n = len(non_matches)

    # display(result[result.score > corner_case_at_least_threshhold].sample(n=n, replace=True, random_state=42)[["name2", "birthdate2", "nat2", "club2"]])

    non_matches[["name2", "birthdate2", "nat2", "club2"]] = result[result.score > corner_case_at_least_threshhold].sample(n=n, replace=True, random_state=42)[["name2", "birthdate2", "nat2", "club2"]].values

    return non_matches