pragma solidity ^0.4.0;

contract MetadataDepository_test01 {

    // **incompleted**
    // Represents an uploaded file, mapping by file ID
    struct File {
        string fileName;
        address owner;
        uint uploadTime;    // timestamp
        string fileType;
        string fileSize;    // Bytes
        string description;
        string metadataHash;
    }

    uint numFiles;
    mapping (uint => File) mappingFiles;

    /// @notice Get number of uploaded files.
    /// @return uint Total uploaded file number.
    function getFileNums() public view returns(uint){
        return numFiles;
    }


    /// @notice Upload com.sh.datasharing.metadata to chain.
    /// @return uint retCode 0: success, 1: metaHash existed, 2: other errors
    /// @return uint fileID An unique file ID
    function uploadMetadata(
        string memory _fileName,
        string memory _fileType,
        string memory _fileSize,
        string memory _description,
        string memory _metadataHash
    ) public returns(
        uint retCode,
        uint fileID
    ) {
        retCode = 0;
        fileID = ++numFiles;

        File storage f = mappingFiles[fileID];

        f.fileName = _fileName;
        f.owner = tx.origin;
        f.uploadTime = block.timestamp;
        f.fileType = _fileType;
        f.fileSize = _fileSize;
        f.description = _description;
        f.metadataHash = _metadataHash;
    }

    /// @notice Return file's com.sh.datasharing.metadata by file ID.
    /// @return file info
    function selectMetadata(uint fileID) public view
    returns(
        string memory _fileName,
        address _owner,
        uint _uploadTime,
        string memory _fileType,
        string memory _fileSize,
        string memory _description,
        string memory _metadataHash
    ){
        File memory targetFile = mappingFiles[fileID];

        _fileName = targetFile.fileName;
        _owner = targetFile.owner;
        _uploadTime = targetFile.uploadTime;
        _fileType = targetFile.fileType;
        _fileSize = targetFile.fileSize;
        _description = targetFile.description;
        _metadataHash = targetFile.metadataHash;
    }

}