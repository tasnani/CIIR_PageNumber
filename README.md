# CIIR_PageNumber
A UMass Center for Intelligent Information Retrieval Project

This an Information Retrieval Project using Machine Learning, Ranking techniques and Hidden Markov Models so far to extract page numbers accurately
out of OCR scanned books. 

Digital libraries do not perform IR on page numbers and find it easier to come up with custom index to organize the pages of a 
book for digital viewing. This is because the kinds of books they receive come in all sorts of conditions. The books used in the project hail from 
the 1600's to the early 1900's. They come damaged with pages ripped or completely missing. It is difficult to extract them. 


The OCR software that scans these books to produce their electronic format can be at fault here. Page numbers maybe interpreted 
incorrectly and be confused with another character or even word. Adding to the complexity of this project is that books have
page numbers in different numbering styles.

Initially, this was a vision problem. The idea would be to perform binary dilation on each image of a book page and perform image recognition on 
large blobs of text. Theroretically this could work, but it is difficult to determine the right threshold of the blob's area. Image 
processing takes a long time as well.

The next approach taken was to use Machine Learning on the XML files of books. A set of features is created to determine how
likely a word on  a page is the page number. A model is created after the ML algorithm learns which word candidates are correct and incorrect.
Running the created model to predict which  test words were page numbers within all pages from different
books had some flaw. In many cases, there would be two word candidates labeled as a page number. This did not return the 
best candidate to be a page number for a given page. 

The next step was to introduce ranking to the algorithm. Ranking would enable the algorithm to select the best word candidate most likely to be a page number for each page
by assigning a overall score of how much a test word is a page number. This produced one best likely output per page.

Currently, when extraction accuracy is high(right page numbers extracted/ total number of possible page numbers is almost equal) many pages will not be accounted for 
because of the damaged states of books. The algorithm can't return a page number that isn't there. A prediction mechanism needs to be 
created so that missing pages are accounted for, repeated pages are readjusted and all are stitched correctly with correctly extracted page numbers
to form a corrected page sequence regardless of the condition of the book. 

Poster about current developements the project: https://drive.google.com/file/d/0B0PA8M0z_bhKTGhYVUtCSEtEOTA/view?usp=sharing

